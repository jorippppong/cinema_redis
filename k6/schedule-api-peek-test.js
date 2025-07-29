import http from "k6/http";
import {check} from "k6";

const BASE_URL = "http://localhost:8080";

// === 1) 사용자 수 기반 트래픽 계산 ===
const N = __ENV.DAU ? parseInt(__ENV.DAU) : 100000;
const dailyConnections = 2 * N;
const avgRPS = dailyConnections / 86400;
const peakRPS = Math.round(avgRPS * 10); // 약 200

// === 2) 테스트 옵션 ===
export const options = {
    scenarios: {
        peakTest: {
            executor: 'constant-arrival-rate',
            rate: peakRPS,  // 1초당 200개의 요청 발생
            timeUnit: '1s',
            duration: '300s',  // 5분간 동작
            preAllocatedVUs: 100,  // 100명의 VU가 초당 200개의 요청을 만듦 (스레드가 100개)
            maxVUs: 100,
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<200'],
        http_req_failed: ['rate<0.01'],
    },
};

// === 3) 테스트 함수 ===
export default function peakTest() {
    let url = `${BASE_URL}/v1/schedule`;
    const requestOptions = {
        headers: {
            "Content-Type": "application/json",
        },
    };

    const genres = ["ACTION", "COMEDY", "DRAMA", "HORROR", "ROMANCE"];
    const randomGenre = genres[Math.floor(Math.random() * genres.length)];
    const titles = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];
    const randomTitle = titles[Math.floor(Math.random() * titles.length)];

    url += `?title=${randomTitle}&genre=${randomGenre}`;

    const response = http.get(url, requestOptions);
    check(response, {
        "status is 200": (r) => r.status === 200,
    });
}
