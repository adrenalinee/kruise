//gradle 기반 build project 생성
pipelineJob("kruise.action.create-project-plain") {
    description("image build and push 를 하는 seed job 을 생성합니다.")
    parameters {
        credentialsParam("projectRepositoryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            description("project repository 인증용 계정 을 지정하세요.")
        }
        stringParam {
            name("projectRepositoryUrl")
            description("빌드를 수행할 소스의 git 주소입니다. ex: TODO")
            trim(true)
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
