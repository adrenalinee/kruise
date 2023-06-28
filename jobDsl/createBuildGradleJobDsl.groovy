pipelineJob("frodo-build-gradle") {
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
        choiceParam {
            name("jdkVersion")
            choices(["17", "20"])
            description("build 를 진행할 jdk의 버전을 지정합니다.")
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
//        disableConcurrentBuilds()
    }
    logRotator {
        numToKeep(30)
    }
}