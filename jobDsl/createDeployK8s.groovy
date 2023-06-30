pipelineJob("frodo-deploy-k8s-kubectl") {
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
        credentialsParam("kubeconfigFile") {
            type("org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl")
            description("deploy 할 kubernetes cluster 의 kubeconfig 파일")
            required(true)
        }
        stringParam {
            name("imageTag")
            description("pull 할 image의 tag 명입니다.")
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
//    properties {
//        disableConcurrentBuilds()
//    }
    logRotator {
        numToKeep(30)
    }
}