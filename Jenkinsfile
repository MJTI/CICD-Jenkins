@Library('CICD-Jenkins') _

pipeline {
    agent any
    tools {
        jdk 'JAVA17'
        maven 'MAVEN3'
    }

    environment {
        SONAR_SERVER = 'sonarserver'
        SONAR_TOKEN = 'sonarlogin'
        NEXUSIP = '172.31.47.168'
        NEXUSPORT = '8081'
        NEXUS_GRP_REPO = 'mjti-maven-group'
        CENTRAL_REPO = 'mjti-maven-central'
        RELEASE_REPO = 'mjti-release'
        SNAP_REPO = 'mjti-snapshot'
    }
    
    stages {
        stage('install'){
            steps {
                mavenBuild()
            }
        }

        stage('test'){
            steps {
                mavenUnitTest()
            }
        }

        stage('checkstyle test'){
            steps{
                mavenCheckstyle()
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