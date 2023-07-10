# Frodo

ci/cd helper


# helm chart
charts 수정후 패키지 갱신을 위해 실행할 명령어

1. 패키지 파일 생성(package 폴더에서)
```shell
cd package
helm package ../charts/[챠트 경로]
```

2. index.yaml 파일 생성 (index.yaml 파일 있는 최상위 경로에서)
```shell
helm repo index .
```
