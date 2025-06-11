export type Lecturer = {
  user_id: string;
  username: string;
  password: string;
  rankingList: RankingList[];
  // commentList: CommentList[];
};
export type RankingList = {
  course_id: string;
  candidateRanking: number[];
};
export type CommentList = {
  tutor_id: string;
  comments: string;
};
export const DEFAULT_LECTURERS: Lecturer[] = [
  {
    user_id: "4",
    username: "big",
    password: "Password123)",
    rankingList: [{ course_id: "COSC1111", candidateRanking: [2, 1] }],
    // commentList: [{ tutor_id: "1", comments:"sounds like a good candidate"}]
  },
  {
    user_id: "5",
    username: "mak",
    password: "Password456)",
    rankingList: [],
  },
];
