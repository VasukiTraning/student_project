pipeline{
    agent {label 'slave'}
    stages{
        stage("checkout code"){
            steps{
                checkout([
                    $class: 'GitSCM',
                    branch:[(name:'*/main')],
                    userRemoteConfig:[[
                        url:"https://github.com/your-repo/your-project.git"
                    ]]
                ])
            }
        }
        stage("build"){
            steps{
                sh '''
                mvn clean install
                '''
            }
        }
        stage("Test"){
            steps{
                sh '''
                mvn Test
                '''
            }
        }
        stage("Depoly to Artifactory"){
            steps{
                configFileProvider([configFile(fileId:'25a9454e-3e36-429e-b7df-8e3a103bb707	',variable: 'MAVEN_SETTINGS')]){
                    sh '''
                    mvn deploy -s $MAVEN_SETTINGS
                    '''
                }
            }
        }
    }
}