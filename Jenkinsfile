pipeline {
    agent any
    tools {
        jdk 'JAVA17'
        maven 'MAVEN3'
    }

    environment {
        SONAR_SERVER = 'sonarserver'
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

        stage('sonar analytics'){
            environment{
                scannerHome = tool 'sonarscanner'
            }
            steps{
                withSonarQubeEnv("$SONAR_SERVER") {
                    sh '''
                    "${scannerHome}"/bin/sonar-scanner \
                    -Dsonar.projectKey=mjti-app \
                    -Dsonar.host.url=http://172.31.45.194 \
                    -Dsonar.token=sonarlogin
                    '''
              }
            }
        }

        stage("Quality Gate"){
            steps{
                timeout(time: 1, unit: 'HOURS') {
                    if (waitForQualityGate().status != 'OK') {
                        error "Pipeline aborted due to quality gate failure: ${waitForQualityGate().status}"
              }
          }
            }
        }
    }
}