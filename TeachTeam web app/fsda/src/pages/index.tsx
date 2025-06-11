import { Container, VStack, Heading } from "@chakra-ui/react";
export default function Home() {
  return (
    <Container maxW="2xl" py={8} flex="1">
      {/* flex=1 causes footer to stay at the bottom */}
      <VStack spacing={8}>
        <Heading>Welcome to the TeachTeam website</Heading>
        <p>
          Log in or sign up to access TeachTeam features such as applying for
          teaching roles or managing job applicants
        </p>
      </VStack>
    </Container>
  );
}
