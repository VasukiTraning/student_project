pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building the project...'
                // Replace this with your actual build command
                sh 'echo Build process goes here'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                // Replace this with your actual test command
                sh 'echo Test process goes here'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying the application...'
                // Replace this with your deployment command
                sh 'echo Deployment process goes here'
            }
        }
    }
}
