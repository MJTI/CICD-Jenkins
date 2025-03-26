def call(Map config = [:]) {
    withCredentials([usernamePassword(credentialsId: 'nexuslogin', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
        sh """
            mvn checkstyle:check -s settings.xml ${config.mavenArgs}
        """
    }
}