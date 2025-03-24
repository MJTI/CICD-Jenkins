pipeline {
    agent any
    tools {
        java 'JAVA17'
        maven 'MAVEN3'
    }
    stages {
        stage('test'){
            steps {
                sh 'pwd && ls -lha'
            }
        }
    }
}