# jenkins

chart: https://artifacthub.io/packages/helm/jenkinsci/jenkins
chart version: 5.7.12 (jenkins version: 2.479.1)

# pre required
## github  oauth app 생성
- Authorization callback URL: {your jenkins home url}/securityRealm/finishLogin

# command
아래 command 에서 ingress host name 을 알맞게 변경해서 사용.

```shell
helm repo add jenkins https://charts.jenkins.io
helm repo update
```

```shell
helm upgrade kruise-jenkins jenkins/jenkins \
  --install \
  --values values.yaml \
  --values kc-security-realm.yaml \
  --set 'controller.ingress.hostName=xxx' \
  --set 'controller.jenkinsUrl=http://xxx' \
  --namespace kruise-jenkins \
  --create-namespace \
  --version 5.7.12
```

# 설치후 최초에 해줘야 할것
설치와 동시에 유일하게 만들어져 있는 'kruise.init' job 실행 

## `kruise.init` job 의 `kruiseRepositoryCredential` 파라미터

kruise 소스를 다운 받을 계정의 token 을 credentials 로 생성해서 셋팅 
- credentials kind: Username with password
- Username: github 계정이름
- Password: 해당 계정으로 발급 받은 Personal access tokens
- token 의 필요한 권한: read:org,repo 


`kruise.init` job 을 실행시키면 kruise 관련 Job 들이 생성된다.
 
# kruise jobs
## `kruise.action.create-project-plain`
- `projectRepositoryCredential` param: 프로젝트 git 인증용 크레덴셜. github access token.
- `containerRegistryCredential` param: container image 를 push 하기 위한 크레덴셜. kic access token.
- `argocdCredential` param: argocd login 용 token.

