pipeline {
    agent any
    stages {
        stage('Checkout Code') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/VasukiTraning/student_project.git'
                    ]]
                ])
            }
        }
        stage('Build and Test in Parallel') {
            parallel {
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
            }
        }
        stage('Deploy to Artifactory') {
            steps {
                configFileProvider([configFile(fileId: '25a9454e-3e36-429e-b7df-8e3a103bb707', variable: 'MAVEN_SETTINGS')]) {
                    sh '''
                    mvn deploy -s $MAVEN_SETTINGS
                    '''
                }
            }
        }
        stage('Deploy to Tomcat') {
            steps {
                sh '''
                cp /home/ubuntu/workspace/pipelinejob1/target/StudentManagementApp-1.0-SNAPSHOT.war /usr/tomcat/tomcat10/webapps
                sudo /usr/tomcat/tomcat10/bin/shutdown.sh
                sudo /usr/tomcat/tomcat10/bin/startup.sh
                '''
            }
        }
    }
}
