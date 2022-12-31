

pipelineJob("${projectName}.build.gradle") {
    parameters {
        stringParam {
            name("projectName")
            defaultValue(projectName)
            trim(true)
        }
        stringParam {
            name("repositoryUrl")
            defaultValue(repositoryUrl)
            trim(true)
        }
        stringParam {
            name("branch")
            defaultValue(branch)
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