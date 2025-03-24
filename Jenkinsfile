pipeline {
    agent any
    tools {
        jdk 'JAVA17'
        maven 'MAVEN3'
    }
    stages {
        stage('install'){
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('test'){
            steps {
                sh 'mvn test'
            }
        }

        stage('checkstyle test'){
            steps{
                sh 'mvn checkstyle:checkstyle'
            }
        }
    }
}