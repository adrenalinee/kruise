

pipelineJob(${projectName}) {
    parameters {
        stringParam {
            name("projectName")
            defaultValue(projectName)
            trim(true)
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url()
                        credentials()
                    }
                    branch("master")
                    scriptPath("jenkinsfile/build-gradle.Jenkinsfile.groovy")
                }
            }
        }
    }
    properties {
        disableConcurrentBuilds()
    }
    logRotator {
        numToKeep(30)
    }
}