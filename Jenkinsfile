pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3.6.3"
    }
    
    triggers {
        githubPush()
    }
    
    parameters {
        string(name: 'GIT_URL', defaultValue: 'https://github.com/kremlsa/otus_qa_task6.git', description: 'The target git url')
        string(name: 'GIT_BRANCH', defaultValue: 'allure', description: 'The target git branch')
        string(name: 'EMAIL_NOTIFICATION', defaultValue: 'kremlsa@yandex.ru', description: 'default email')
    }

    stages {
        
        stage('Pull project from GitHub') {
            steps {
                git ([
                    url: "${params.GIT_URL}",
                    branch: "${params.GIT_BRANCH}"
                    ])
            }
        }
        
        stage('Run maven clean test') {
            steps {
                sh 'mvn clean test'
                slackSend(message: "Notification from Jenkins Pipeline Start job '${env.JOB_NAME}' from repo '${params.GIT_URL}'")
            }
        }
    }
    
    post {
        always {
            emailext body: readFile("target/surefire-reports/emailable-report.html"), mimeType: 'text/html', subject: "Job '${env.JOB_NAME}' (${env.BUILD_NUMBER}) from '${GIT_URL}/${GIT_BRANCH}' ended with ${currentBuild.currentResult}", to: ${EMAIL_NOTIFICATION}
            
            allure([
                  includeProperties: false,
                  jdk: '',
                  properties: [],
                  reportBuildPolicy: 'ALWAYS',
                  results: [[path: 'target/allure-results']]
                ])
        }
    }
}
