# 미니 메이플스토리 프로토타입 개발 태스크

## 1단계: 프로젝트 구조 설정
- [ ] `src` 디렉터리 내에 서버(`server`)와 클라이언트(`client`), 공유 모델(`common`)을 위한 패키지 구조 생성
- [ ] 스펙 문서에 명시된 각 Java 파일들 생성 (`GameServer.java`, `ClientHandler.java`, `GameClient.java` 등)

## 2단계: 공유 데이터 모델 구현
- [ ] `common` 패키지에 플레이어, 몬스터의 상태를 표현하는 데이터 클래스 구현 (e.g., `PlayerState.java`, `MonsterState.java`)
- [ ] `common` 패키지에 통신 프로토콜(JSON)을 객체로 변환하기 위한 클래스 구현 (e.g., `GameData.java`)

## 3단계: 서버 핵심 기능 구현
- [ ] `GameServer.java`: 클라이언트 접속을 받고, 각 클라이언트를 위한 `ClientHandler` 스레드 생성
- [ ] `ClientHandler.java`: 클라이언트로부터 `PLAYER_UPDATE` 메시지 수신 및 처리
- [ ] `GameState.java`: 모든 플레이어와 몬스터의 상태를 실시간으로 관리
- [ ] `GameLoop.java`: 주기적으로 몬스터의 상태를 업데이트하고, 모든 클라이언트에게 `GAME_STATE` 브로드캐스팅

## 4단계: 클라이언트 핵심 기능 구현
- [ ] `GameClient.java`: 서버에 소켓 연결
- [ ] `GameFrame.java` & `GamePanel.java`: 게임 기본 창과 렌더링 영역(JPanel) 구현
- [ ] `NetworkHandler.java`: 서버로부터 `GAME_STATE` 메시지를 받아 `GamePanel`의 데이터 업데이트
- [ ] `GamePanel.java`: 키보드 입력을 감지하여 서버로 `PLAYER_UPDATE` 전송
- [ ] `GamePanel.java`의 `paintComponent`: `GAME_STATE`에 따라 모든 캐릭터와 몬스터를 화면에 렌더링
- [ ] `SpriteManager.java`: 캐릭터 및 몬스터의 GIF 리소스를 로드하고 관리하는 로직 구현

## 5단계: 통합 및 테스트
- [ ] 서버와 클라이언트를 실행하여 다중 클라이언트 접속 테스트
- [ ] 한 클라이언트의 움직임이 다른 클라이언트에 동기화되는지 확인
- [ ] 서버에서 생성된 몬스터가 모든 클라이언트에 정상적으로 나타나고 움직이는지 확인
