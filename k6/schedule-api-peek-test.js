import http from "k6/http";
import {check} from "k6";

// === 1) 사용자 수 기반 트래픽 계산 ===
const N = __ENV.DAU ? parseInt(__ENV.DAU) : 100000;
const dailyConnections = 2 * N;
const avgRPS = dailyConnections / 86400;
const peakRPS = Math.round(avgRPS * 10);

// === 2) 테스트 옵션 ===
export const options = {
    scenarios: {
        peakTest: {
            executor: 'constant-arrival-rate',
            rate: peakRPS,
            timeUnit: '1s',
            duration: '300s',
            preAllocatedVUs: 500,
            maxVUs: 1500,
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<200'],
        http_req_failed: ['rate<0.01'],
    },
};

// === 3) 테스트 함수 ===
export default function peakTest() {
    const requestOptions = {
        headers: {
            "Content-Type": "application/json",
        },
    };

    const titles = ["SY", "AB", "CD", "EF", "GH"];
    const genres = ["ACTION", "COMEDY", "DRAMA", "HORROR", "ROMANCE"];

    // 랜덤하게 title 선택 (50% 확률로 없음)
    const useTitle = Math.random() < 0.5;
    const titleParam = useTitle
        ? `title=${titles[Math.floor(Math.random() * titles.length)]}`
        : "";

    // 랜덤하게 genre 선택 (50% 확률로 없음)
    const useGenre = Math.random() < 0.5;
    const genreParam = useGenre
        ? `genre=${encodeURIComponent(genres[Math.floor(Math.random() * genres.length)])}`
        : "";

    // 최종 URL 조립
    let url = "http://host.docker.internal:8080/movie";
    const query = [titleParam, genreParam].filter(q => q).join("&");
    if (query) url += "?" + query;

    const response = http.get(url, requestOptions);
    check(response, {
        "status is 200": (r) => r.status === 200,
    });
}
