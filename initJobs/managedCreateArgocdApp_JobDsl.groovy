//argocd application 생성
pipelineJob("kruise.managed.create-argocd-app") {
    description("argocd app 을 갱신합니다.")
    parameters {
        stringParam {
            name("argocdProjectName")
            description("argocd 프로젝트 이름입니다.")
            trim(true)
        }
        stringParam {
            name("argocdApplicationName")
            description("argocd 어플리케이션 이름입니다.")
            trim(true)
        }
        stringParam {
            name("clusterName")
            description("argocd application 을 배포할 클러스터 이름입니다. kruise-argocd 에 등록할때 지정한 이름입니다. in-cluster 는 kruise-argocd 가 설치된 클러스터입니다.")
        }
        stringParam {
            name("releaseName")
            description("argocd application 의 helm chart 의 릴리즈이름입니다. k8s 리소스의 이름에 사용됩니다.")
        }
        stringParam {
            name("helmChartName")
            description("kruise 에 정의된 helm chart name 입니다.")
            trim(true)
        }
        textParam {
            name("helmChartValues")
            description("위에서 선택한 helmChart 에서 변경할 values 를 지정합니다. (yaml 포맷)")
        }
        stringParam {
            name("imagePath")
            description("container image 경로 입니다.")
            trim(true)
        }
        booleanParam {
            name("override")
            description("기존에 이미 같은 이름으로 argocd application 이 만들어져 있을때, 덮어쓸지 여부입니다. ")
        }
        credentialsParam("argocdCredential") {
            type("org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl")
            defaultValue(argocdCredential)
            description("argocd 인증 토큰 을 지정하세요.")
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
                    scriptPath("initJobs/managedCreateArgocdApp.Jenkinsfile")
                }
            }
        }
    }
}