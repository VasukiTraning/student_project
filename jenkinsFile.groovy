pipeline {
    agent { label 'slave' } // Specify the label for the agent (slave)
    stages {
        stage("Checkout Code") {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']], // Fixed syntax for branches
                    userRemoteConfigs: [[
                        url: "https://github.com/your-repo/your-project.git" // Replace with your repo
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
