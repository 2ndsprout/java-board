package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class BoardApp {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    Scanner scan = new Scanner(System.in);
    int latestArticleId = 4; // 시작 번호를 1로 지정
    ArrayList<Article> articleList = new ArrayList<>(); // 인스턴스 변수
    ArrayList<Customer> customerList = new ArrayList<>();
    Customer loggedInUser = null;

    public void run() {

        testMask();

        while (true) { // 반복 조건이 true이기 때문에 무한 반복

            System.out.print("명령어");
            if (loggedInUser != null) {
                System.out.print("[" + loggedInUser.getId() + "(" + loggedInUser.getNickname() + ")]");
            }
            System.out.print(" : ");
            String cmd = scan.nextLine();

            if (cmd.equals("exit")) { // 숫자가 아닌 경우 같다라는 표현을 할 때 == 이 아닌 .equals()를 사용해야 한다.
                System.out.println("프로그램을 종료합니다.");
                break; // 반복문 탈출

            } else if (cmd.equals("add")) {

                add();

            } else if (cmd.equals("list")) {

                list();

            } else if (cmd.equals("update")) {

                update();

            } else if (cmd.equals("delete")) {

                delete();

            } else if (cmd.equals("detail")) {

                detail();

            } else if (cmd.equals("search")) {

                search();
            } else if (cmd.equals("signup")) {

                signup();

            } else if (cmd.equals("login")) {

                login();

            }
        }
    }

    private void login() {

        System.out.print("아이디 : ");
        String id = scan.nextLine();
        System.out.print("비밀번호 : ");
        String password = scan.nextLine();

        for (Customer customer : customerList) {
            if (customer.getId().equals(id) && customer.getPassword().equals(password)) {
                loggedInUser = customer;
                System.out.println(loggedInUser.getNickname() + "님 환영합니다!");
                return;
            }
        }
        System.out.println("아이디나 비밀번호가 틀렸거나 잘못된 회원정보입니다.");


    }

    private void signup() {
        System.out.println("==== 회원 가입을 진행합니다 ====");
        System.out.print("아이디를 입력해주세요 : ");
        String id = scan.nextLine();
        System.out.print("비밀번호를 입력해주세요 : ");
        String password = scan.nextLine();
        System.out.print("닉네임을 입력해주세요 : ");
        String nickname = scan.nextLine();

        Customer customer = new Customer(id, password, nickname);
        customerList.add(customer);

        System.out.println("==== 회원가입이 완료되었습니다. ====");
    }

    public void printArticleList(ArrayList<Article> targetList) {
        for (int i = 0; i < targetList.size(); i++) {

            Article article = targetList.get(i);

            System.out.println("번호 : " + article.getId());
            System.out.printf("제목 : %s\n", article.getTitle());
            System.out.println("===================");
        }
    }

    private void search() {
        // 검색어를 입력
        System.out.println("검색 키워드를 입력해주세요 :");
        String keyword = scan.nextLine();

        ArrayList<Article> searchedList = new ArrayList<>();

        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            if (article.getTitle().contains(keyword)) {
                searchedList.add(article);
            }
        }
        printArticleList(searchedList);
    }

    private void testMask() {

        Article a1 = new Article(1, "안녕하세요 반갑습니다. 자바 공부중이에요.", "인사", 0, 0, getCurrentDateTime());
        Article a2 = new Article(2, "자바 질문좀 할게요~", "질문", 0, 0, getCurrentDateTime());
        Article a3 = new Article(3, "정처기 따야되나요?", "문의", 0, 0, getCurrentDateTime());

        articleList.add(a1);
        articleList.add(a2);
        articleList.add(a3);
    }

    private void detail() {

        System.out.print("상세보기 할 게시물 번호를 입력해주세요 : ");
        int inputId = Integer.parseInt(scan.nextLine());

        int index = findIndexById(inputId);

        if (index == -1) {
            System.out.println("없는 게시물입니다.");
            return;
        }
        Article article = articleList.get(index);
        article.increaseView();
        ArrayList<Comments> commentsList = article.getComments();

        // alt + ctrl + L : 코드 정리. 자주 사용할 것
        System.out.println("====================");
        System.out.println("번호 : " + article.getId());
        System.out.println("제목 : " + article.getTitle());
        System.out.println("내용 : " + article.getBody());
        System.out.println("등록 날짜 : " + article.getRegDate());
        System.out.println("조회수 : " + article.getView());
        if (article.getHit() < 0) {
            System.out.println("추천수 : " + article.getHit());
        }
        System.out.println("====================");
        if (commentsList != null) {
            System.out.println("======= 댓글 =======");
            for (Comments comment : commentsList) {
                System.out.println("댓글 내용 : " + comment.getComments());
                System.out.println("댓글 등록 날짜 : " + comment.getCommentsRegDate());
                System.out.println("====================");
            }
        }

        System.out.println("상세보기 기능을 선택해주세요.\n1. 댓글 등록\n2. 추천\n3. 수정\n4. 삭제\n5. 목록으로");
        int choice = Integer.parseInt(scan.nextLine());

        switch (choice) {
            case 1:
                System.out.print("댓글을 입력하세요 : ");
                String commets = scan.nextLine();
                String commentRegDay = getCurrentDateTime();

                if (commentsList == null) {
                    commentsList = new ArrayList<>();
                    article.setComments(commentsList);
                }
                article.addComment(commets, commentRegDay);
                System.out.println("댓글 등록이 완료되었습니다.");

                break;

            case 2:
                article.increaceHit();
                System.out.println("추천 되었습니다.");

                break;

            case 3:
                System.out.print("수정하실 제목을 입력해주세요 : ");
                String updateTitle = scan.nextLine();
                article.setTitle(updateTitle);
                System.out.print("수정하실 내용을 입력해주세요 : ");
                String updateBody = scan.nextLine();
                article.setBody(updateBody);
                System.out.println(index + "번 게시물 수정이 완료되었습니다.");
        }


    }

    private void delete() {
        System.out.print("삭제할 게시물 번호를 입력해주세요 : ");
        int inputId = Integer.parseInt(scan.nextLine());

        int index = findIndexById(inputId);

        if (index == -1) {
            System.out.println("없는 게시물입니다.");
            return;
        }

        articleList.remove(index);
        System.out.printf("%d 게시물이 삭제되었습니다.\n", inputId);
    }

    private void add() {
        System.out.print("게시물 제목을 입력해주세요 : ");
        String title = scan.nextLine();

        System.out.print("게시물 내용을 입력해주세요 : ");
        String body = scan.nextLine();
        String regDate = getCurrentDateTime();

        // 모든 매개변수를 받는 생성자 이용


        if (loggedInUser != null) {

            Article article = new Article(latestArticleId, title, body, 0, 0, regDate);
            loggedInUser.addArtcleList(article);
        } else {

            Article article = new Article(latestArticleId, title, body, 0, 0, regDate);
            articleList.add(article);
        }
        System.out.println("게시물이 등록되었습니다.");
        latestArticleId++; // 게시물이 생성될 때마다 번호를 증가
    }

    public void list() {
        System.out.println("====================");

        for (int i = 0; i < articleList.size(); i++) {

            Article article = articleList.get(i);
            System.out.println("번호 : " + article.getId());
            System.out.println("제목 : " + article.getTitle());
            if (article.getRegDate() != null) {

                System.out.println("등록 날짜 : " + article.getRegDate());
                System.out.println("====================");
            }
        }

        if (loggedInUser != null) {

            ArrayList<Article> userArticles = loggedInUser.getArticleList();

            if (userArticles.isEmpty()) {

                System.out.println("작성한 게시물이 없습니다.");

            } else {

                for (Article article : userArticles) {

                    System.out.println("번호 : " + article.getId());
                    System.out.println("제목 : " + article.getTitle());
                    if (article.getRegDate() != null) {
                        System.out.println("등록 날짜 : " + article.getRegDate());
                        System.out.println("====================");

                    }
                }
            }
        }
    }

    public void update() {
        System.out.print("수정할 게시물 번호를 입력해주세요 : ");
        int inputId = Integer.parseInt(scan.nextLine());

        int index = findIndexById(inputId);
        if (index == -1) {
            System.out.println("없는 게시물입니다.");
            return;
        }

        System.out.print("새로운 제목을 입력해주세요 : ");
        String newTitle = scan.nextLine();

        System.out.print("새로운 내용을 입력해주세요 : ");
        String newBody = scan.nextLine();

        Article target = articleList.get(index);
        target.setTitle(newTitle); // target은 참조값이므로 직접 객체를 접근하여 수정 가능
        target.setBody(newBody);

        System.out.printf("%d번 게시물이 수정되었습니다.\n", inputId);

    }

    // 입력 : 찾고자 하는 게시물 번호
    // 출력 : 게시물 번호에 해당하는 인덱스
    public int findIndexById(int id) {

        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);

            if (article.getId() == id) {
                return i; // 원하는 것은 찾은 즉시 종료.
            }
        }
        return -1;
    }

    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();

        // 날짜와 시간의 형식을 지정합니다. 여기서는 연-월-일 시:분:초 형식을 사용합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

        // 지정한 형식으로 날짜와 시간을 출력합니다.
        String formattedDate = now.format(formatter);

        return formattedDate;
    }
}
