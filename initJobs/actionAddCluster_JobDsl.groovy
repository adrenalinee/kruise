//외부 클러스터 연결하는 job 생성
pipelineJob("kruise.action.add-cluster") {
    description("argocd 에 외부 k8s 클러스터를 연결합니다. 외부 클러스터에 배포해야 할때 필요합니다.")
    parameters {
        stringParam {
            name("serverUrl")
            description("k8s cluster API 서버의 URL 입니다.")
            trim(true)
        }
        stringParam {
            name("contextName")
            description("argocd 에 등록될 외부 cluster 의 이름 입니다.")
            trim(true)
        }
        textParam {
            name("caCertificate")
            description("k8s cluster API 서버의 인증서를 검증하는 데 사용되는 클러스터 인증 기관입니다. 매개변수가 제공되지 않으면 검증이 건너뜁니다. base64 인코딩되지 않은 원본입력해야 합니다.")
        }
        credentialsParam("kruiseAdminToken") {
            type("org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl")
            description("k8s cluster 접속에 사용할 token 을 입력하세요.")
        }

        credentialsParam("argocdCredential") {
            type("org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl")
            defaultValue(argocdCredential)
            description("argocd 인증 토큰 을 지정하세요.")
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
                    scriptPath("initJobs/actionAddCluster.Jenkinsfile")
                }
            }
        }
    }
}