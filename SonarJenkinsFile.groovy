pipeline {
    agent any
	  tools {
   
        maven 'maven'
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/VasukiTraning/student_project.git',
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
        stage('SonarQube Analysis') {
            environment {
               SONAR_HOST_URL = 'http://51.21.150.248:9000'
			   SONAR_AUTH_TOKEN =credentials('SonarQube')
            }
			steps{
			sh '''
			mvn sonar:sonar -Dsonar.projectkey=sample_project -Dsonar.host.url=$SONAR_HOST_URL -Ds 
			'''
			}
        }
        stage('Deploy to Tomcat') {
            steps {
                sh '''
                cp /home/ubuntu/workspace/pipelinejob1/target/StudentManagementApp-2.0-SNAPSHOT.war /usr/tomcat/tomcat10/webapps
                sudo /usr/tomcat/tomcat10/bin/shutdown.sh
                sudo /usr/tomcat/tomcat10/bin/startup.sh
                '''
            }
        }
    }
}
