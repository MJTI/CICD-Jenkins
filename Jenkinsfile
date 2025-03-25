pipeline {
    agent any
    tools {
        jdk 'JAVA17'
        maven 'MAVEN3'
    }

    environment {
        SONAR_SERVER = 'sonarserver'
        SONAR_TOKEN = 'sonarlogin'
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
                withSonarQubeEnv("${SONAR_SERVER}") {
                    sh '''
                    "${scannerHome}"/bin/sonar-scanner \
                    -Dsonar.projectKey=mjti-app \
                    -Dsonar.projectName=mjti-project \
                    -Dsonar.projectVersion=1.0 \
                    -Dsonar.java.binaries=target/test-classes/com/ \
                    -Dsonar.sources=src/ \
                    -Dsonar.junit.reportsPath=target/surefire-reports/ \
                    -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                    -Dsonar.java.checkstyle.reportsPaths=target/checkstyle-result.xml'''
              }
            }
        }

        stage("Quality Gate"){
            steps{
                script{
                    timeout(time: 1, unit: 'HOURS') {
                        if (waitForQualityGate().status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${waitForQualityGate().status}"
                        }
                    }
                }
            }
        }

    }
}