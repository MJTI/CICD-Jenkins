pipeline {
    agent any
    tools {
        jdk 'JAVA17'
        maven 'MAVEN3'
    }

    environment {
        SONAR_SERVER = 'sonarserver'
        SONAR_TOKEN = 'sonarlogin'
        NEXUS_LOGIN = 'nexuslogin'
        NEXUSIP = '172.31.47.168'
        NEXUSPORT = '8081'
        NEXUS_GRP_REPO = 'mjti-maven-group'
        CENTRAL_REPO = 'mjti-maven-central'
        RELEASE_REPO = 'mjti-release'
        SNAP_REPO = 'mjti-snapshot'
    }
    
    stages {
        stage('Build'){
            steps {
                withCredentials([usernamePassword(credentialsId: "${NEXUS_LOGIN}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    sh 'mvn clean install -s settings.xml -DskipTest'
                }
            }
            post {
                success {
                    echo 'Now Archiving...'
                    archiveArtifacts artifacts: '**/target/*.war'
                }
            }
        }

        stage('Unit Test'){
            steps {
                withCredentials([usernamePassword(credentialsId: "${NEXUS_LOGIN}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    sh 'mvn test -s settings.xml'
                }
            }
        }

        stage('Checkstyle Test'){
            steps{
                withCredentials([usernamePassword(credentialsId: "${NEXUS_LOGIN}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    sh 'mvn checkstyle:checkstyle -s settings.xml'
                }
            }
        }

        stage('Upload Reports'){
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

        stage('Quality Gate'){
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

        stage('Upload Artifact'){
            steps{
                //script {
                def warFile = findFiles(glob: 'target/*.war')
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: "${NEXUSIP}" + ":" + "${NEXUSPORT}",
                    groupId: 'com.mjti',
                    version: "${BUILD_TIMESTAMP}",
                    repository: "${RELEASE_REPO}",
                    credentialsId: "${NEXUS_LOGIN}",
                    artifacts: [
                        [artifactId: 'mjti-app',
                         classifier: '',
                         file: "target/${warFile}",
                         type: 'war']
                    ]
                )
                //}
            }
        }
    }
}