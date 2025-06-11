export type Application = {
  course_id: string | undefined;
  tutor_id: string | undefined;
  role: string;
  message: string;
};

export const DEFAULT_APPLICATIONS: Application[] = [
  // COSC1111 applications
  {
    course_id: "COSC1111",
    tutor_id: "1",
    role: "tutor",
    message: "Hi, hire me or I sad",
  },
  {
    course_id: "COSC1111",
    tutor_id: "2",
    role: "tutor",
    message: "Passionate about algorithms, would love to tutor this course.",
  },
  {
    course_id: "COSC1111",
    tutor_id: "3",
    role: "lab",
    message: "Experienced with lab tutoring in Python.",
  },
  {
    course_id: "COSC1111",
    tutor_id: "4",
    role: "tutor",
    message: "Completed this course with HD, eager to help students succeed.",
  },
  {
    course_id: "COSC1111",
    tutor_id: "5",
    role: "lab",
    message: "Comfortable leading labs and answering student questions.",
  },
  {
    course_id: "COSC1111",
    tutor_id: "6",
    role: "tutor",
    message: "I’ve tutored this unit before and received great feedback.",
  },
  {
    course_id: "COSC1111",
    tutor_id: "7",
    role: "lab",
    message: "I want to help students avoid the mistakes I made.",
  },
  {
    course_id: "COSC1111",
    tutor_id: "8",
    role: "tutor",
    message: "I enjoy explaining programming concepts to beginners.",
  },

  // COSC2222 applications
  {
    course_id: "COSC2222",
    tutor_id: "1",
    role: "tutor",
    message: "Bye, hire me or I mad",
  },
  {
    course_id: "COSC2222",
    tutor_id: "2",
    role: "tutor",
    message: "Bye, don't hire me or I mad",
  },
  {
    course_id: "COSC2222",
    tutor_id: "3",
    role: "lab",
    message: "I enjoy hands-on sessions and supporting practical learning.",
  },
  {
    course_id: "COSC2222",
    tutor_id: "4",
    role: "tutor",
    message: "Previously tutored advanced topics — ready to step in again.",
  },
  {
    course_id: "COSC2222",
    tutor_id: "5",
    role: "lab",
    message: "Lab sessions are my strength; students always feel supported.",
  },
];
