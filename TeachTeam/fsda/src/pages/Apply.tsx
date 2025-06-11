import {
  Alert,
  AlertDescription,
  AlertIcon,
  Button,
  Flex,
  Heading,
  Select,
  Textarea,
  VStack,
} from "@chakra-ui/react";
import { useRouter } from "next/router";
import { useState, useEffect, useRef } from "react";
import { useCourses } from "@/context/CourseContext";
import { statusObj } from "@/types/statusObj";
import { Application } from "@/types/applications";
import { useAuth } from "@/context/AuthContext";
import { useApplications } from "@/context/ApplicationContext";

export default function apply() {
  const router = useRouter();
  const { courseId } = router.query; // Access the courseId from the query
  const { courses } = useCourses();
  const { applications, setApplications } = useApplications();
  const { user } = useAuth();

  const [submitLock, setSubmitLock] = useState(false);

  // Get the course that user is currently applying to
  const currentCourse = courses.find((course) => course.course_id === courseId);
  const [currentApplication, setCurrentApplication] = useState<Application>({
    course_id: currentCourse?.course_id,
    tutor_id: user?.user_id,
    role: "",
    message: "",
  });
  const [applicationStatus, setApplicationStatus] = useState<statusObj>({
    status: undefined,
    desc: "",
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Checks if role is selected
    if (!currentApplication.role) {
      setApplicationStatus({
        status: "error",
        desc: "Please select a role before submitting.",
      });
    }
    // Check for empty cover message
    else if (!currentApplication.message.trim()) {
      setApplicationStatus({
        status: "error",
        desc: "Please enter a cover message.",
      });
    } else {
      // Success and valid application is to be submitted
      const updatedApplications = [...applications, currentApplication];
      setApplications(updatedApplications);
      localStorage.setItem("applications", JSON.stringify(updatedApplications));
      setApplicationStatus({
        status: "success",
        desc: "Successfully submitted application!",
      });
      setSubmitLock(true);
    }
  };

  return (
    <Flex flex="1" justify="center" my={4}>
      <VStack
        maxWidth="9xl"
        border="1px"
        borderColor="gray.300"
        borderRadius="md"
        p={10}
      >
        <Heading size="md" width="30rem" textAlign="center">
          {currentCourse?.course_id} - {currentCourse?.course_name}
        </Heading>
        <Select
          placeholder="Select role"
          width="10rem"
          py="1rem"
          value={currentApplication.role}
          onChange={(e) =>
            setCurrentApplication((prev) => ({ ...prev, role: e.target.value }))
          }
        >
          <option value="tutor">Tutor</option>
          <option value="lab">Lab Assistant</option>
        </Select>
        <Heading size="sm">Cover Message</Heading>
        <Textarea
          size="sm"
          value={currentApplication.message}
          onChange={(e) =>
            setCurrentApplication((prev) => ({
              ...prev,
              message: e.target.value,
            }))
          }
        />
        <Button
          size="md"
          colorScheme="pink"
          onClick={handleSubmit}
          isDisabled={submitLock ? true : false}
        >
          Submit Application
        </Button>
        {applicationStatus.status && (
          <Alert status={applicationStatus.status} variant="left-accent">
            <AlertIcon />
            <AlertDescription>{applicationStatus.desc}</AlertDescription>
          </Alert>
        )}
      </VStack>
    </Flex>
  );
}
