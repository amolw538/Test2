
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Environment') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Run Selenium Tests') {
            steps {
                sh 'mvn clean test'
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
