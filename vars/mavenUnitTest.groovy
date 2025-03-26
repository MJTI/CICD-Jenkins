def call(Map config = [:]) {
    withCredentials([usernamePassword(credentialsId: 'nexuslogin', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
        sh """
            mvn test -s settings.xml
        """
    }
}