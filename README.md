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

1. **예약 추가시 메세지 큐를 전송하고, 이를 전달 받아 처리하는 과정 동시성 이슈 발생**

- Redisson 을 활용한 분산락으로 메세지 큐 전송 과정을 하나씩 처리하고 DB 저장 전 데이터를 캐싱해 활용하도록 변경
- 예약 추가의 처리량을 유지하며 원자적 처리 보장

2. **요청 처리 중 서버가 다운되어 데이터가 유실 되는 문제 발생**

- RabbitMQ 를 도입하여 요청을 큐로 저장하고 이를 서버로 전송하도록 변경
- 서버 다운시에도 고객 데이터 유실 방지 및 예약 순서 보장
- [상세정보](https://taekt.tistory.com/35)

3. **S3 업로드 실패 시 데이터 유실되는 문제 발생**

- 외부 저장소 장애 시 이미지를 DB에 임시 저장하고, 정상 작동 확인 후 스케줄러로 자동 업로드
- 외부 저장소의 상태 여부와 관련 없이 데이터를 안전하게 보관, 안정성 확보
- [상세정보](https://taekt.tistory.com/34)

4. **페이지 증가에 따른 반환 속도 저하 문제 발생**

- 기존 Jpa Page -> QueryDSL 을 활용한 Keyset Pagination 방식 변경
- 일관된 속도 유지 및 최소 데이터 조회로 효율성 증가
- [상세정보](https://taekt.tistory.com/33)

5. **서버 용량 부족으로 인한 백업 처리 한계 문제 발생**

- 기존 서버 저장 방식 → Shell Script와 Docker Volume을 활용해 Github 저장으로 변경
- 서버 부담 감소 및 데이터의 접근 용이성 확보

6. **스케줄러를 활용한 미사용 Token 제거로 인한 서버 부담 증가**

- Token 저장소 Mysql → Redis로 변경 후 TTL 적용
- 자동 만료 관리로 유지보수 용이성 증가 및 서버 부담 감소






