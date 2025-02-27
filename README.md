# Kruise

모험을 꿈꾸시나요? Kruise 는 당신을 배포의 세계로 안내할 준비가 되어 있습니다.
당신은 푸른 바다를 탐험하며 그 진정한 아름다움을 발견할 것입니다.
지금, 모험가로 변신했을 때입니다. 당신의 이야기가 여기서 시작됩니다.


루틴에서 벗어나 환상적인 여정을 떠나보세요.
Kruise 는 색다른 경험을 선사합니다.
당신은 푸른 바다 위를 여행하며 자유롭게 휴식을 취하고 새로운 모험을 찾을 수 있습니다.
이제 여행의 마법에 빠져보세요.

# Information
- 빌드/배포 환경: kubernetes
- 빌드/배포 방법: jenkins job 실행
- 빌드 방식: gradle, docker image build/push
- 배포 방식: argo-cd 에 helm chart 방식으로 배포

jenkins job 을 통해 프로젝트 빌드후 argocd 에 application 생성 및 배포까지 진행시켜줌.



# How to install
**step 1: helm 으로 argo-cd, jenkins 설치**




**step 2: jenkins 로그인해서 'kruise.init' job 실행**
사전준비: kruise 소스 저장소에 접근하기 위한 credential 필요.
- credential kind: Username with password
- kruise 소스 저장소(github)에서 Personal access tokens 을 발급받아서 사용
- repo 그룹 접근권한 필요.



**step 3: 테스트 application 배포용 seed job 실행**
사전준비1: application 소스 저장소에 접근하기 위한 credential 필요.
- credential kind: Username with password
- kruise 소스 저장소(github)에서 Personal access tokens 을 발급받아서 사용
- repo 그룹 접근권한 필요.

사전준비2: 빌드된 이미지 push 할 container registry 인증용 credential 필요.
- credential kind: Username with password

사전준비3: k8s 에 deploy 를 진행하기 위해 argocd 인증용 credential 필요.
- credential kind: Username with password
- argocd access token 생성해서 사용.
- argocd access token 생성방법:


# Global setting

## $KUBECONFIG
helm chart 가 설치될 cluster config 파일을 지정하는 환경변수입니다.
$KUBECONFIG 환경변수에 지정된 cluster 에 helm chart 가 설치되도록 할 수 있습니다.

> https://helm.sh/docs/helm/helm/#helm

command 실행전에 아래와 같이 $KUBECONFIG 환경변수를 설정해야 합니다.

```shell
export KUBECONFIG={kubeconfig yaml 파일 경로}
```

아래와 같이 명령으로 확인
```shell
echo $KUBECONFIG
```