pipeline {
    agent any

    environment {
        // Define Docker Hub repository name and image tag
        DOCKERHUB_CREDENTIALS_ID = 'docker_credentials'
        DOCKERHUB_REPO = 'annagaom/ohtuprojekti'
        DOCKER_IMAGE_TAG = 'ver1'
    }

    tools {
        // Docker tool from Jenkins global configuration
        dockerTool 'dokerHub'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from Git repository
                git url: 'https://github.com/PetraHauk/OhtuProjekti.git', branch: 'main'
            }
        }
         stage('Run Tests') {
            steps {
                // Run the tests first to generate data for Jacoco and JUnit
                bat 'mvn clean test' // For Windows agents
                // sh 'mvn clean test' // Uncomment if on a Linux agent
            }
        }
        stage('Code Coverage') {
            steps {
                // Generate Jacoco report after the tests have run
                bat 'mvn jacoco:report'
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
                    // Build Docker image
                    docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
                    steps {
                        script {
                            withEnv(["PATH=/usr/local/bin:/usr/bin:/bin"]) { // Ensure correct PATH is used
                                withCredentials([usernamePassword(credentialsId: 'docker_credentials', // Change to your credential ID
                                                                 usernameVariable: 'DOCKER_USER',
                                                                 passwordVariable: 'DOCKER_PASS')]) {
                                    sh """
                                        docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}
                                        docker build -t ${DOCKER_USER}/ohtuprojekti:ver1 .
                                        docker push ${DOCKER_USER}/ohtuprojekti:ver1
                                        docker logout
                                    """
                                }
                            }
                        }
                    }
                }
    }
}
