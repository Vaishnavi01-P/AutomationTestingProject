pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk   'JDK-17'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '15'))
        disableConcurrentBuilds()
    }

    environment {
        HEADLESS_MODE = 'true'
        BROWSER = 'chrome'
        SUITE_XML = 'testng.xml'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from Git repository...'
                checkout scm
            }
        }

        stage('Environment Check') {
            steps {
                echo 'Validating runtime environment tools...'
                script {
                    if (isUnix()) {
                        sh 'mvn -version'
                        sh 'java -version'
                    } else {
                        bat 'mvn -version'
                        bat 'java -version'
                    }
                }
            }
        }

        stage('Build Project') {
            steps {
                echo 'Compiling test automation framework sources...'
                script {
                    if (isUnix()) {
                        sh 'mvn clean compile test-compile'
                    } else {
                        bat 'mvn clean compile test-compile'
                    }
                }
            }
        }

        stage('Execute Automation Tests') {
            steps {
                echo "Running TestNG Suite [${env.SUITE_XML}] on Browser: ${env.BROWSER} (Headless: ${env.HEADLESS_MODE})..."
                script {
                    if (isUnix()) {
                        sh "mvn test -DsuiteXmlFile=${env.SUITE_XML} -Dbrowser=${env.BROWSER} -Dheadless=${env.HEADLESS_MODE}"
                    } else {
                        bat "mvn test -DsuiteXmlFile=${env.SUITE_XML} -Dbrowser=${env.BROWSER} -Dheadless=${env.HEADLESS_MODE}"
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Collecting and archiving test execution results and reports...'

            // Publish TestNG/Surefire JUnit XML results
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'

            // Archive HTML ExtentReports and Surefire Reports
            archiveArtifacts allowEmptyArchive: true, artifacts: 'reports/**, target/surefire-reports/**, test-output/**'

            // Archive failure screenshots if any were generated
            archiveArtifacts allowEmptyArchive: true, artifacts: 'screenshots/*.png'
        }
        success {
            echo '=============================================='
            echo 'SUCCESS: All Automation Tests Passed!'
            echo '=============================================='
        }
        failure {
            echo '=============================================='
            echo 'FAILURE: One or more Automation Tests Failed!'
            echo 'Please inspect archived reports and screenshots.'
            echo '=============================================='
        }
        cleanup {
            cleanWs deleteDirs: true, notFailBuild: true
        }
    }
}
