# argo-cd

chart: https://artifacthub.io/packages/helm/argo/argo-cd
(argo-cd version: v2.14.2)

# command
아래 command 에서 ingress host name 을 알맞게 변경해서 사용.

```shell
helm repo add argo https://argoproj.github.io/argo-helm
helm repo update
```

```shell
helm upgrade kruise-argocd argo/argo-cd \
  --install \
  --values values.yaml \
  --namespace kruise-argocd \
  --create-namespace \
  --version 7.8.5
```



# account
기본 admin 계정과 kruise 관리용 kruise-admin 계정이 생성되어 있음.
admin 계정은 web 로그인 가능.
kruise-admin 계정은 api(cli 포함) 로만 로그인 가능하고 argocd web 으로는 로그인 불가.
익명 유저는 모든 view 권한을 가지고 있음.


## kruise-admin 계정 token 생성

1. 기본 생성된 admin 계정으로 web 로그인.
2. Settings 메뉴 -> Accounts 들어가면 admin, kruise-admin 두개 계정 보임.
3. kruise-admin 계정선택 -> tokens 패널 -> Generate New 클릭.
4. 생성된 토큰 밑에서 확인. 화면 갱신되면 더이상 토큰 볼 수 없음.


# repository
argocd 에서 접근하는 저장소에 인증이 필요할 경우에 argocd application 생성전에 인증정보를 저장해놔야 한다.
1. 기본 생성된 admin 계정으로 web 로그인.
2. Settings 메뉴 -> Repositories 들어감.
3. connect repo 버튼 클릭 후 저장소 주소와 인증정보(아이디, 비밀번호)를 입력. github 의 경우 비밀번호로 github 비밀번호외에 계정 access token 도 사용할 수 있다.


# cluster
argocd 가 설치된곳이 아닌 외부 cluster 에 배포를 하려면 argocd 에 외부 클러스터를 등록해주어야 한다.

외부 클러스터 등록은 web 에서는 불가능하고 cli 를 통해서만 가능하다.

