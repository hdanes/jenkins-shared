def call(body) {
    // evaluate the body block, and collect configuration into the object
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    pipeline {
      agent any
      stages {
          /* "Build" and "Test" stages omitted */

          stage('Deploy - Staging') {
              steps {
                  sh './deploy staging'
                  sh './run-smoke-tests'
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
