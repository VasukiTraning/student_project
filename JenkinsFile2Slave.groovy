pipeline {
    agent none // Define agent at the top-level as none to specify agents per stage
    stages {
        stage('Checkout Code') {
            agent { label 'Slave' } // Run this stage on a specific slave
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
        stage('Build') {
            agent { label 'Slave1' } // Run the build stage on slave2
            steps {
                sh '''
                mvn clean install
                '''
            }
        }
        stage('Test') {
            agent { label 'Slave' } // Run the test stage on slave1
            steps {
                sh '''
                mvn test
                '''
            }
        }
        stage('Deploy to Artifactory') {
            agent { label 'Slave' } // Deploy from slave2
            steps {
                configFileProvider([configFile(fileId: '25a9454e-3e36-429e-b7df-8e3a103bb707', variable: 'MAVEN_SETTINGS')]) {
                    sh '''
                    mvn deploy -s $MAVEN_SETTINGS
                    '''
                }
            }
        }
        stage('Deploy to Tomcat') {
            agent { label 'Slave' } // Deploy from slave2
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
