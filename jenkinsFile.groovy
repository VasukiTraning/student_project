pipeline {
    agent any
    stages {
        stage("Checkout Code") {
            steps {
               checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    gitTool: 'Default Git', // Name configured in Global Tool Configuration
                    userRemoteConfigs: [[
                        url: 'https://github.com/VasukiTraning/student_project.git'
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
