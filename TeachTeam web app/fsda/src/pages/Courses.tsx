import { useAuth } from "@/context/AuthContext";
import { useCourses } from "@/context/CourseContext";
import {
  Box,
  Button,
  Card,
  CardBody,
  CardHeader,
  Flex,
  Heading,
  HStack,
  Stack,
  StackDivider,
} from "@chakra-ui/react";
import { useRouter } from "next/router";

interface RoleType {
  buttonMsg: string;
  handler: (courseId: string) => void;
}
export default function CoursesPage() {
  const router = useRouter();
  const { courses } = useCourses();
  const { user } = useAuth();
  // Ensures user context data is loaded before doing any operations on it

  if (!user) {
    return (
      <Flex flex="1" justify="center" align="center">
        <Heading size="md">Loading user data...</Heading>
      </Flex>
    );
  }

  /* Passes URL parameters to the apply page so that the corresponding 
              course is referenced in the apply page */
  const handleApply = (courseId: string) => {
    router.push(`/Apply?courseId=${courseId}`);
  };

  const handleHire = (courseId: string) => {
    router.push(`/Hire?courseId=${courseId}`);
  };
  const isTutor = "aCredentials" in user;
  const roleType: RoleType = isTutor
    ? { buttonMsg: "Apply", handler: handleApply }
    : { buttonMsg: "Hire", handler: handleHire };

  return (
    <Flex flex="1" justify="center">
      <Stack
        divider={<StackDivider />}
        spacing="4"
        pt="5"
        height="600px"
        overflowY="auto"
        overflowX="hidden"
        width="52rem"
      >
        {/* Maps the list of course objects to a card component display */}
        {courses.map((course) => (
          <Card key={course.course_id} width="50rem">
            <HStack justify="space-between" pr="1rem">
              {/* Wraps the text in a box so that button can be centered in the middle to its right 
              with HStack */}
              <Box>
                <CardHeader>
                  <Heading size="md">{course.course_id}</Heading>
                </CardHeader>
                <CardBody>{course.course_name}</CardBody>
              </Box>
              <Button
                onClick={() => roleType.handler(course.course_id)}
                colorScheme="blue"
              >
                {roleType.buttonMsg}
              </Button>
            </HStack>
          </Card>
        ))}
      </Stack>
    </Flex>
  );
}
