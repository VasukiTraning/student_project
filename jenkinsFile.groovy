pipeline {
    agent { label 'Slave' }
    stages {
        stage('Checkout Code') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/NirupSingh2377/sample-webapp.git',
                     ]]
                ])
            }
        }
        stage('Build') {
            steps {
                sh '''
                mvn clean install
                '''
            }
        }
        stage('Test') {
            steps {
                sh '''
                mvn test
                '''
            }
        }
        stage('Deploy to Artifactory') {
            steps {
                configFileProvider([configFile(fileId: 'cf03c01a-a8c1-48f0-b40b-5a9bd4ae3232', variable: 'MAVEN_SETTINGS')]) {
                    sh '''
                    mvn deploy -s $MAVEN_SETTINGS
                    '''
                }
            }
        }
    }
}
