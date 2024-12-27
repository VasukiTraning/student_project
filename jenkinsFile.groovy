pipeline {
    agent { label 'Slave' } 
    stages {
        stage("Checkout Code") {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']], 
                    userRemoteConfigs: [[
                        url: "https://github.com/your-repo/your-project.git",
                        credentialsId:""
                    ]]
                ])
            }
        }
        stage("Build") {
            steps {
                sh '''
                mvn clean install
                '''
            }
        }
        stage("Test") {
            steps {
                sh '''
                mvn test // Corrected `mvn Test` to `mvn test` (case-sensitive)
                '''
            }
        }
        stage("Deploy to Artifactory") {
            steps {
                configFileProvider([configFile(fileId: '25a9454e-3e36-429e-b7df-8e3a103bb707', variable: 'MAVEN_SETTINGS')]) {
                    sh '''
                    mvn deploy -s $MAVEN_SETTINGS
                    '''
                }
            }
        }
    }
}
