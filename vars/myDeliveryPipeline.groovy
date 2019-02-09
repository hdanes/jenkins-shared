def call(body) {
    // evaluate the body block, and collect configuration into the object
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    pipeline {
      agent any
      stages {
          stage('Test') {
              steps {
                  sh 'make test'
              }
          }

          stage('Sanity check') {
              steps {
                  input "Does the staging environment look ok?"
              }
          }

          stage('Deploy - Production') {
              steps {
                  sh './deploy production'
              }
          }
      }
    }
}
