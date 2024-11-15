pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = 'annagaom/ohtuprojekti' // Docker Hub repository
        DOCKER_IMAGE_TAG = 'latest'   // Docker image tag
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/PetraHauk/OhtuProjekti.git', branch: 'anna-3-test' // Change to your Git repository
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
