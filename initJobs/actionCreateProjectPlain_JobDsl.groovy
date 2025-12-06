//gradle 기반 build project 생성
pipelineJob("kruise.action.create-project-plain") {
    description("image build and push 를 하는 seed job 을 생성합니다.")
    parameters {
        credentialsParam("projectRepositoryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            description("project repository 인증용 계정 을 지정하세요.")
        }
        stringParam {
            name("projectName")
            description("생성 할 프로젝트 이름입니다. 다른 프로젝트와 이름이 겹치지 않게 주의해주세요. 덮어쓰기가 될 수 있습니다. ex: centerflow-op")
            trim(true)
        }
        stringParam {
            name("projectRepositoryUrl")
            description("빌드를 수행할 소스의 git 주소입니다. ex: TODO")
            trim(true)
        }
        stringParam {
            name("projectRepositoryBranch")
            defaultValue("")
            description("빌드를 수행할 branch 입니다. 입력하지 않으면 config 의 기본값을 사용합니다.")
            trim(true)
        }
        stringParam {
            name("clusterName")
            defaultValue("")
            description("argocd application 을 배포할 클러스터 이름입니다. 입력하지 않으면 config 의 기본값을 사용합니다.")
        }
        stringParam {
            name("phase")
            description("argocd application, k8d resource name 의 뒤에 붙을 접미사. (optional) 같은 project 안에서 배포단위별로 추가적인 접미사를 지정할 수 있습니다. ex: dev, cbt, prod")
        }
        stringParam {
            name("imagePath")
            defaultValue("")
            description("image push 할 주소 입니다. 입력하지 않으면 config 의 기본값을 사용합니다.")
            trim(true)
        }
        textParam {
            name("helmChartValues")
            description("Helm values override 입니다. 입력하지 않으면 config 의 기본값을 사용합니다.")
            defaultValue("")
        }
        booleanParam {
            name("override")
            description("기존에 이미 같은 이름으로 argocd application 이 만들어져 있을때, 덮어쓸지 여부입니다. ")
        }

        credentialsParam("containerRegistryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            defaultValue(containerRegistryCredential)
            description("containerRegistry 인증용 계정을 지정하세요.")
        }
        credentialsParam("kruiseRepositoryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            defaultValue(kruiseRepositoryCredential)
            description("빌드 스크립트를 다운받을때 사용할 인증 token 을 지정하세요.")
        }
        stringParam {
            name("kruiseRepositoryUrl")
            defaultValue(kruiseRepositoryUrl)
            description("빌드 스크립트를 가져올 git 주소입니다.")
            trim(true)
        }
        stringParam {
            name("kruiseBranch")
            defaultValue(kruiseBranch)
            description("빌드 스크립트를 가져올 branch 입니다.")
            trim(true)
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(kruiseRepositoryUrl)
                        credentials(kruiseRepositoryCredential)
                    }
                    branch(kruiseBranch)
                    scriptPath("initJobs/actionCreateProjectPlain.Jenkinsfile")
                }
            }
        }
    }
}
