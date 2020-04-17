pipeline {
    agent any
    stages {
        stage('Compile Source Code') {
            steps {
                sh './mvnw compile'
            }
        }
        stage('Run Tests') {
            steps {
                sh './mvnw test'
            }
        }
        stage('SonarQube Quality Check') {
             steps {
                sh "./mvnw clean install sonar:sonar -Dsonar.branch=${env.BRANCH_NAME}"
            }
        }
     }
}