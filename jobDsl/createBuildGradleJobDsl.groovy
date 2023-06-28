pipelineJob("build-gradle") {
    parameters {
        stringParam {
            name("projectName")
            defaultValue("hello-world")
            trim(true)
        }
        stringParam {
            name("repositoryUrl")
            defaultValue("https://github.com/adrenalinee/hello-world-kotlin.git")
            trim(true)
        }
        stringParam {
            name("branch")
            defaultValue("develop")
            trim(true)
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(frodoRepositoryUrl)
                        credentials(frodoCredential)
                    }
                    branch(frodoBranch)
                    scriptPath("jenkinsfile/build-gradle.Jenkinsfile")
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