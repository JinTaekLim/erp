# ERP 란?

> 운전 면허 매장 회원/예약 관리 WEB 사이트 

- 기간 : 2024.10 ~ 2024.12
- 구성 : FE 2, BE 1, Designer 1
- [기획/UI](https://www.figma.com/design/kplN35VPPaPC1LSfMvOAtA/%5B-ERP-%5D?node-id=0-1&t=GvDgwRCKznwXJCly-1)

## 기술 스택
- Backend
    - Java, Spring Boot, Jpa, Spring Security, JWT, Swagger
- DB
    - MySQL, Redis
- DevOps / Infra
    - AWS EC2, Docker, GitHub Actions, Nginx, RabbitMQ

## 아키텍처
![ERP-Architecture drawio]( https://github.com/user-attachments/assets/15a2f79a-34c9-4fe3-8e56-98d753b5f941)

![ERP-Layered](https://github.com/user-attachments/assets/ca5637f7-86bb-46ea-8ffd-8822dd33c4b8)

## 트러블슈팅

1. **스케줄러를 활용한 미사용 Token 제거로 인한 서버 부담 증가**

- Token 저장소 Mysql → Redis로 변경 후 TTL 적용
- 자동 만료 관리로 유지보수 용이성 증가 및 서버 부담 감소

2. **컨트롤러 단에서 API의 역할 파악이 어려운 문제 발생**

- 커스텀 어노테이션과 인터셉터를 활용해 역할 분리
- API의 역할을 직관적으로 확인 가능하여 유지보수성 향상

3. **서버 용량 부족으로 인한 백업 처리 한계 문제 발생**

- 기존 서버 저장 방식 → Shell Script와 Docker Volume을 활용해 Github 저장으로 변경
- 서버 부담 감소 및 데이터의 접근 용이성 확보

4. **S3 업로드 실패 시 데이터 유실되는 문제 발생**

- 외부 저장소 장애 시 이미지를 DB에 임시 저장하고, 정상 작동 확인 후 스케줄러로 자동 업로드
- 외부 저장소의 상태 여부와 관련 없이 데이터를 안전하게 보관, 안정성 확보