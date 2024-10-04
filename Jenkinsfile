pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = 'annagaom/ohtuprojekti' // Docker Hub repository
        DOCKER_IMAGE_TAG = 'latest' // Docker image tag
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/PetraHauk/OhtuProjekti.git', branch: 'anna' // Change to your Git repository
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Adding Docker's path to the environment
                    withEnv(["PATH=/usr/local/bin:/usr/bin:/bin"]) {
                        // Build the Docker image
                        docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                    }
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
                            // Login to Docker Hub and push the image
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
