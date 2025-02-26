-- Bulletin Board Database Dump
-- Created for Finshot Code Test Project
-- PostgreSQL Database Script

-- Drop the table if it exists (be careful with this in production environments)
DROP TABLE IF EXISTS posts;

-- Create the posts table
CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    views INTEGER DEFAULT 0,
    is_deleted BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

-- Add comments to table and columns for better documentation
COMMENT ON TABLE posts IS 'Stores all bulletin board posts';
COMMENT ON COLUMN posts.id IS 'Unique identifier for each post, auto-incremented';
COMMENT ON COLUMN posts.title IS 'Post title (up to 50 chars in Korean, 100 in English)';
COMMENT ON COLUMN posts.content IS 'Main content of the post';
COMMENT ON COLUMN posts.author IS 'Name of the post author (up to 10 chars)';
COMMENT ON COLUMN posts.password IS 'Password for editing or deleting the post';
COMMENT ON COLUMN posts.views IS 'Number of times the post has been viewed';
COMMENT ON COLUMN posts.is_deleted IS 'Flag to indicate if post is deleted (true) or active (false)';
COMMENT ON COLUMN posts.created_at IS 'Timestamp when the post was created';
COMMENT ON COLUMN posts.updated_at IS 'Timestamp when the post was last modified';

-- Create index for faster queries
CREATE INDEX idx_posts_created_at ON posts(created_at DESC);
CREATE INDEX idx_posts_is_deleted ON posts(is_deleted);

-- Insert sample data for testing
INSERT INTO posts (title, content, author, password, views, is_deleted, created_at, updated_at)
VALUES 
    ('게시판에 오신 것을 환영합니다', '새로운 게시판 시스템에 첫 번째 게시물입니다. 사용하기 쉽고 유용하게 사용하시길 바랍니다. 자유롭게 게시물을 작성하고 대화에 참여해 주세요!', 'hapid', 'hapid', 15, false, NOW() - INTERVAL '7 days', NULL),
    
    ('게시판 사용 방법', '새 게시물을 작성하려면 페이지 상단의 "게시물 작성" 버튼을 클릭하세요. 이름, 제목, 게시물 내용을 입력해야 합니다. 나중에 수정하거나 삭제하려면 게시물의 비밀번호를 설정해야 합니다.', 'hapid', 'hapid', 12, false, NOW() - INTERVAL '6 days', NOW() - INTERVAL '5 days'),
    
    ('커뮤니티 가이드라인', '이 게시판에 게시할 때는 예의를 지켜주세요. 불쾌한 내용이나 스팸을 게시하지 마세요. 반복적인 위반은 게시물이 삭제될 수 있습니다.', 'hapid', 'hapid', 8, false, NOW() - INTERVAL '5 days', NULL),
    
    ('소개: 새 회원', '안녕하세요! 저는 이 커뮤니티에 새로 왔습니다. 자기소개를 하고 싶었어요. 여러분과 토론하고 아이디어를 공유하기를 기대합니다.', 'hapid', 'hapid', 5, false, NOW() - INTERVAL '4 days', NULL),
    
    ('기술 토론: 스프링 프레임워크', '최근에 스프링 부트로 작업하고 있는데 웹 애플리케이션 개발에 매우 강력하다고 느꼈습니다. 경험이 있으신 분 계신가요? 의존성 주입에 대한 생각은 어떠신가요?', 'hapid', 'hapid', 7, false, NOW() - INTERVAL '3 days', NULL),
    
    ('게시판에 대한 피드백', '이 게시판의 디자인이 정말 마음에 들어요! 깔끔하고 사용하기 쉽습니다. 한 가지 제안을 하자면 특정 게시물을 쉽게 찾을 수 있도록 검색 기능을 추가하는 것이 좋을 것 같습니다.', 'hapid', 'hapid', 4, false, NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day'),
    
    ('PostgreSQL에 대한 질문', 'PostgreSQL을 처음 사용하고 있는데 쿼리 최적화에 대한 모범 사례에 대해 궁금합니다. 이에 대한 경험이 있으신 분 계신가요?', 'hapid', 'hapid', 3, false, NOW() - INTERVAL '1 day', NULL),
    
    ('삭제된 게시물 예시', '이것은 삭제된 게시물의 예시입니다. 목록에는 표시되지 않지만 데이터베이스에는 여전히 존재합니다.', 'hapid', 'hapid', 1, true, NOW() - INTERVAL '8 days', NOW() - INTERVAL '7 days'),
    
    ('주말 계획', '이번 주말에 특별한 계획 있으신가요? 저는 새로운 등산로를 시도해 볼 계획입니다!', 'hapid', 'hapid', 2, false, NOW() - INTERVAL '12 hours', NULL),
    
    ('책 추천', '로버트 C. 마틴의 "클린 코드"를 읽었는데 매우 통찰력 있었습니다. 다른 프로그래밍 도서 추천 있으신가요?', 'hapid', 'hapid', 6, false, NOW() - INTERVAL '5 hours', NULL),
    
    ('프로젝트 마감일 접근 중', '다음 금요일이 프로젝트 마감일임을 상기시켜 드립니다. 제 시간에 맞춰 제출해 주세요!', 'hapid', 'hapid', 9, false, NOW() - INTERVAL '2 hours', NULL),
    
    ('카페 추천', '내일 일하기 좋은 카페를 찾고 있습니다. 도심 지역에 추천할 만한 곳이 있나요?', 'hapid', 'hapid', 1, false, NOW() - INTERVAL '1 hour', NULL);

-- Add a few more posts to test pagination
INSERT INTO posts (title, content, author, password, views, is_deleted, created_at, updated_at)
VALUES 
    ('PostgreSQL 배우기', '프로젝트를 위해 PostgreSQL을 배우고 있습니다. 매우 강력하지만 학습 곡선이 가파릅니다. 초보자를 위한 팁이 있을까요?', 'hapid', 'hapid', 3, false, NOW() - INTERVAL '30 minutes', NULL),
    
    ('자바스크립트 프레임워크 비교', 'React, Angular, Vue.js를 비교해 보신 분 계신가요? 새 프로젝트에 어떤 것을 추천하시겠어요?', 'hapid', 'hapid', 5, false, NOW() - INTERVAL '25 minutes', NULL),
    
    ('원격 근무 팁', '지난 1년 동안 원격으로 일해왔습니다. 생산성을 유지하는 데 도움이 된 몇 가지 팁을 공유합니다...', 'hapid', 'hapid', 7, false, NOW() - INTERVAL '20 minutes', NULL),
    
    ('버그 추적 도구', '프로젝트에서 어떤 버그 추적 도구를 사용하시나요? Jira를 고려하고 있지만 대안에 대해 알고 싶습니다.', 'hapid', 'hapid', 2, false, NOW() - INTERVAL '15 minutes', NULL),
    
    ('자바 학습 자료', '고급 자바 프로그래밍을 위한 좋은 학습 자료를 추천해주실 수 있나요? 책, 강의 또는 웹사이트가 도움이 될 것 같습니다.', 'hapid', 'hapid', 4, false, NOW() - INTERVAL '10 minutes', NULL),
    
    ('도커 대 쿠버네티스', '도커와 쿠버네티스의 차이점을 이해하려고 합니다. 어떤 경우에 하나를 다른 것보다 선호하시나요?', 'hapid', 'hapid', 3, false, NOW() - INTERVAL '5 minutes', NULL);
