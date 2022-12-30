

pipelineJob("${projectName}.build.gradle") {
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
                        url(frodoRepositoryUrl)
                    }
                    branch(frodoBranch)
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