pipeline {
    agent any

    environment {
        // Define Docker Hub repository name and image tag
        DOCKERHUB_CREDENTIALS_ID = 'docker_credentials'
        DOCKERHUB_REPO = ''annagaom/ohtuprojekti'
        DOCKER_IMAGE_TAG = 'latest'
    }

    tools {
        // Docker tool from Jenkins global configuration
        dockerTool 'Docker Hub'
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
                    // Switch to the 'default' Docker context
                    bat 'docker context use default'

                    // Push the Docker image to Docker Hub using Jenkins credentials
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub_credential') {
                        docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }
    }
}
