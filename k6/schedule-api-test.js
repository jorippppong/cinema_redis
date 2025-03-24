import http from 'k6/http';
import {check, sleep} from 'k6';

// 가상 사용자 수 (피크 시간대)
const VUSER_COUNT = 833; // 활성 유저 수 (피크 시간대 기준)

// 각 유저당 반복 횟수
const REQUESTS_PER_USER = 3; // 각 유저가 반복 요청하는 횟수

// 각 요청 간 대기 시간 (5분 동안 3회 요청을 기준으로 설정)
const SLEEP_TIME = 1; // 1초 대기

export let options = {
    stages: [
        {duration: '5m', target: VUSER_COUNT},  // 5분 동안 833명의 유저를 목표로 한다.
    ],
    thresholds: {
        // TPS와 Latency, Error rate에 대한 기준
        'http_reqs': ['count > 2500'],  // 요청 수가 2500회 이상이어야 한다.
        'http_req_duration': ['p(95)<500'],  // 95%의 요청이 500ms 이하로 응답해야 한다.
        'http_req_failed': ['rate<0.01'],  // 에러율이 1% 미만이어야 한다.
    },
};

export default function () {
    // 요청을 반복 수행 (각 유저는 3번 요청)
    for (let i = 0; i < REQUESTS_PER_USER; i++) {
        let res = http.get('http://localhost:8080/v1/schedule');  // 테스트할 API 엔드포인트로 변경
        check(res, {
            'is status 200': (r) => r.status === 200, // 성공적인 응답인지 확인
        });

        // 요청 사이에 1초 대기 (또는 다른 대기 시간 설정)
        sleep(SLEEP_TIME);
    }
}
