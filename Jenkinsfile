pipeline {
    agent any

    environment {
            DOCKERHUB_REPO = 'annagaom/ohtuprojekti' // Docker Hub repository
            DOCKER_IMAGE_TAG = 'latest'   // Docker image tag
        }

    tools {
        // Docker tool from Jenkins global configuration
        dockerTool 'Docker Hub'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from Git repository
                git url: 'https://github.com/PetraHauk/OhtuProjekti.git', branch: 'anna-1'
            }
        }
         stage('Run Tests') {
            steps {
                // Run the tests first to generate data for Jacoco and JUnit
                sh 'mvn clean test' // For Windows agents
                // sh 'mvn clean test' // Uncomment if on a Linux agent
            }
        }
        stage('Code Coverage') {
            steps {
                // Generate Jacoco report after the tests have run
                sh 'mvn jacoco:report'
            }
        }
        stage('Publish Test Results') {
            steps {
                // Publish JUnit test results
                junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('Publish Coverage Report') {
            steps {
                // Publish Jacoco coverage report
                jacoco()
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withEnv(["PATH=/usr/local/bin:/usr/bin:/bin"]) {
                        // Build the Docker image for a specific platform
                        sh """
                            docker buildx build --platform linux/amd64 \
                            -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG} .
                        """
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
           steps {
               script {
                   withEnv(["PATH=/usr/local/bin:/usr/bin:/bin"]) {
                       // Use Jenkins credentials to log in to Docker Hub
                       withCredentials([usernamePassword(
                           credentialsId: 'docker_credentials', // Replace with your Jenkins credentials ID
                           usernameVariable: 'DOCKER_USER',
                           passwordVariable: 'DOCKER_PASS'
                       )]) {
                           sh """
                               docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}
                               docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}
                               docker logout
                           """
                       }
                   }
               }
           }
        }
    }
}