
pipeline {
    agent any

    triggers {
    pollSCM('H/5 * * * *')
    }


    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Environment') {
            steps {
                sh 'java -version'
                sh '/usr/local/bin/mvn -version'
            }
        }

        stage('Run Selenium Tests') {
            steps {
                sh '/usr/local/bin/mvn clean test'
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/TEST-*.xml',
            allowEmptyResults: true

            archiveArtifacts artifacts: '*.png',
            allowEmptyArchive: true
        }

        success {
            echo 'MediStack Selenium tests passed!'
        }

        failure {
            echo 'MediStack Selenium tests failed. Check the console output.'
        }
    }
}
