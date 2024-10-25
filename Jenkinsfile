
pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = 'annagaom/ohtuprojekti'    // Docker Hub repository
        DOCKER_IMAGE_TAG = 'ver1'          // Docker image tag
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/PetraHauk/OhtuProjekti.git', branch: 'anna' // Change to your Git repository
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Adding Docker's path to the environment
                    withEnv(["PATH=/usr/local/bin:/usr/bin:/bin"]) {
                       // docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                        //docker buildx build --platform linux/amd64 -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                           sh """
                                docker buildx build --platform linux/amd64 -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG} .
                           """
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
                            sh """
                                docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}
                                docker build -t ${DOCKER_USER}/ohtuprojekti:latest .
                                docker push ${DOCKER_USER}/ohtuprojekti:latest
                                docker logout
                            """
                        }
                    }
                }
            }
        }
    }
}
