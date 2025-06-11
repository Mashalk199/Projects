import { Tutor } from "./tutors";

export type Course = {
  course_id: string;
  course_name: string;
};

export const DEFAULT_COURSES: Course[] = [
  {
    course_id: "COSC1111",
    course_name: "Full Stack Development",
  },
  {
    course_id: "COSC2222",
    course_name: "Artificial Intelligence",
  },
  {
    course_id: "COSC3333",
    course_name: "Introduction to Cloud Computing",
  },
  {
    course_id: "COSC4444",
    course_name: "Advanced Programming in Python",
  },
];
